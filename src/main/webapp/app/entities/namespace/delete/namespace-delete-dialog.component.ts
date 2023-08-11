import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { INamespace } from '../namespace.model';
import { NamespaceService } from '../service/namespace.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './namespace-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NamespaceDeleteDialogComponent {
  namespace?: INamespace;

  constructor(protected namespaceService: NamespaceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.namespaceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
