<template>
  <q-list :class="buttononly ? '' : 'q-pa-sm q-pb-md q-gutter-sm rounded-borders'" flat :bordered="!buttononly">
    <div v-if="!buttononly">
      <q-item class="q-my-none q-pl-none">
        <q-item-label header class="q-pt-sm q-pb-none">{{ $t(label) }}</q-item-label>
      </q-item>
      <slot name="disclaimer"/>
      <q-table v-if="!parent" class="q-mt-none" :rows="selected.map(t => tagdata.find(d => d.id === t))" :row-key="name" grid
               :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <TagLink suppresselems v-if="linkable" :key="props.row.name" :elemtype="url.includes('user') ? 'user' : 'event'" v-bind="props.row" right-icon="clear" @deleteElem="(id) => $emit('tagDeleted', id)"/>
          <TagElem suppresselems v-else :key="props.row.name" v-bind="props.row">
            <template v-slot:right>
              <q-icon name="clear" @click="selected.splice(selected.indexOf(props.row.id), 1)"/>
            </template>
          </TagElem>
        </template>
        <template v-slot:no-data>
          {{ $t('tag.none') }}
        </template>
      </q-table>
      <TagElem v-else-if="selected" v-bind="tagdata.find(d => d.id === selected)"></TagElem>
      <q-item-label v-else caption class="q-pa-md"> {{ $t('tag.no_parent') }} </q-item-label>
    </div>
  <q-btn color="primary" :flat="buttononly" @click="dialog = true">
    {{ $t(buttononly ? 'edit' : 'tag.add') }}
  </q-btn>
  <q-dialog v-model="dialog" @before-hide="$emit('tagsSelected', this.selected)">
    <q-card style="width: 50%">
      <q-card-section v-if="!parent">
        <q-item-label caption> {{ $t('tag.selected')}} </q-item-label>
        <q-badge v-for="tag in selected.map(t => tagdata.find(d => d.id === t))" class="q-mr-xs" rounded :style="tag.color ? `background: ${tag.color}` : `background: primary`" :label="tag.name" v-bind:key="tag.id"/>
      </q-card-section>
      <q-card-section v-else>
        <q-item-label caption> {{ $t(selected ? 'tag.parent' : 'tag.no_parent') }}</q-item-label>
        <q-badge v-if="selected !== null" rounded :label="tagdata.find(d => d.id === selected).name" :style="'background: ' + tagdata.find(d => d.id === selected).color || 'primary'"/>
      </q-card-section>
      <q-separator/>
      <q-card-section style="max-height: 70vh" class="scroll">
    <q-tree width="100%"
            :nodes="tagoptions"

      node-key="id"
      label-key="name"
      dense

>
<template v-slot:default-header="prop">
<TagElem
  :elems="[...prop.node.events]"
  :name="prop.node.name" :id="prop.node.id"
  :color="prop.node.color"
  :ticked="parent ? prop.node.id === this.selected  : this.selected.includes(prop.node.id)"
  elemtype="event"
  @click.stop="updateSelected(this, prop.node.id)"/>
</template>
</q-tree>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn color="primary" v-close-popup> {{ $t('submit')}}</q-btn>
      </q-card-actions>
</q-card>
</q-dialog>
  </q-list>
  <!--<q-select v-if="parent" menu-self="bottom start" menu-anchor="top start" :label=" (parent ? $t('tag.parent') : $t('tag.many')) + $t('optional')" :multiple="!parent" use-chips use-input :counter="!parent" v-model="selected" :options="tagoptions" option-value="id" option-label="name" @filter="filterTags"/>-->
</template>

<script>
import {api} from "boot/axios";
import {getErrorMessage} from "src/util";
import TagElem from "components/TagElem.vue";
import TagLink from "components/TagLink.vue";

function union(set1, set2) {
  set2.forEach(e => set1.add(e))
}

function pluckChildren(elem, array) {
  elem.childrenIds.forEach((c) => {

    const child = array.find(e => c === e.id)
    if (child === undefined) return

    pluckChildren(child, array)
    elem.children.push(child)
    union(elem.events, child.events)
    array.splice(array.indexOf(child), 1)
  })
}

function treeify(tags) {
  const res = []
  while (tags.length > 0) {
    const tag = tags.find(t => t.parentId === null)
    tags.splice(tags.indexOf(tag), 1)
    pluckChildren(tag, tags)
    res.push(tag)
  }
  console.log(res)
  return res
}

function updateTree(self) {
  api
    .get(self.url)
    .then((response) => {
      self.tagdata = response.data.map(t => {return {
        name: t.name,
        id: t.id,
        color: t.color
      }})
      self.tagoptions = treeify(response.data.map(function(item) {return {
        name: item.name,
        id: item.id,
        events: new Set(item.eventIds),
        color: item.color,
        children: [],
        childrenIds: item.childrenIds,
        parentId: item.parentId,
        noTick: true,
        ticked:false,
      }}))})
    .catch(error => self.$q.notify(self.$t(getErrorMessage(error))))
}

export default {
  name: "TagSelect",
  components: {TagElem, TagLink},
  data() {
    return {
      dialog: false,
      tagdata: null,
      tagoptions: [],
      selected: this.parent ? null : [],
      oneselected: null,
      lastselected: null
    }
  },
  mounted() {
    updateTree(this)
  },
  props: {
    url: {
      type: String,
      required: true
    },
    parent: {
      type: Boolean
    },
    defaultSelected: {
      type: Object
    },
    label: {
      type: String,
      default: "tag.many"
    },
    linkable: {
      type: Boolean
    },
    buttononly : {
      type: Boolean
    }
  }
  ,
  methods: {
    updateTree(self) {
      return updateTree(self)
    },
    treeify(tags) {
      return treeify(tags)
    },
    updateSelected(self, target) {
      if (self.parent) {
        if (self.selected === target) {
               self.selected = null;
        } else self.selected = target;
        return
      }
      console.log(self)
      console.log(target)
      if (target != null) self.lastselected = target;
      const index = self.selected.indexOf(target);
      const lastindex = self.selected.indexOf(self.lastselected)
      if (index !== -1) {
        self.selected.splice(index, 1)
      } else if (target != null) {
        self.selected.push(target)
      } else if (lastindex === -1) {
        self.selected.push(self.lastselected)
      } else {
        self.selected.splice(lastindex, 1)
      }
    }
  }
}
</script>
